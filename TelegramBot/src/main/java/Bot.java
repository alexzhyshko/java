import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import model.ModelController;
import model.Product;
import model.DBUser;

public class Bot extends TelegramLongPollingBot {

	private static Bot bot;

	private static ModelController controller;

	private static String name = "stekloKPI_bot";
	private static String token = "803561124:AAHrpTM866eRaLhZ4AxC_2FHQqNS1Ph0UKY";

	private Bot() {
		controller = ModelController.get();
	}

	public static Bot getBot() {
		if (bot == null) {
			bot = new Bot();
		}
		return bot;
	}

	public void onUpdateReceived(Update update) {

		SendMessage sendMessage = new SendMessage();
		String chatId = update.getMessage().getChatId().toString();
		sendMessage.enableMarkdown(true);
		sendMessage.setChatId(chatId);

		String message = update.getMessage().getText();
		if (message == null && update.getMessage().getContact() == null) {
			sendMessage.setText("Я таких команд не знаю");
			sendMsg(sendMessage, update);
			return;
		}
		String id = update.getMessage().getFrom().getId().toString();

		int userstate = controller.getUserState(id);
		switch (userstate) {
		case 0:

			setMenuButtons(sendMessage);
			sendMessage.setText("Что делаем?");
			sendMsg(sendMessage, update);
			String userid = update.getMessage().getFrom().getId().toString();
			String name = update.getMessage().getFrom().getFirstName();
			String username = update.getMessage().getFrom().getUserName();
			String surname = update.getMessage().getFrom().getLastName();
			if (!controller.userExists(userid)) {
				controller.saveUser(userid, username, name, surname, -1);
			}

			controller.incrementUserState(id);
			break;
		case 1:
			if (message.toLowerCase().equals("каталог")) {
				sendMessage.setText("Выберите товар");
				setProductButtons(sendMessage, controller.getAllGoods());
				sendMsg(sendMessage, update);
				controller.incrementUserState(id);
			} else if (message.toLowerCase().equals("мои заказы")) {
				List<Product> orders = controller.getUserOrders(id);
				String text = "Вот Ваши заказы за все время \n \n";
				for (Product p : orders) {
					text += "- Заказ на " + p.getName() + ", цена " + p.getPrice() + " грн. \n";
				}
				sendMessage.setText(text);
				setOrderAgainButtons(sendMessage);
				sendMsg(sendMessage, update);
				controller.setUserState(id, 0);
			} else if (message.toLowerCase().equals("мой промокод")) {
				controller.generatePromoForUser(id);
				String generatedPromo = controller.generatePromoForUser(id);
				sendMessage.setText(generatedPromo != null ? "Ваш промокод успешно сгенерирован: " + generatedPromo
						: "Ваш промокод: " + controller.getUserPromo(id));
				quitButtons(sendMessage);
				sendMsg(sendMessage, update);
				controller.setUserState(id, 0);
			} else if (message.toLowerCase().equals("ввести промокод")) {
				sendMessage.setText("Введите промокод друга, который Вас пригласил");
				quitButtons(sendMessage);
				sendMsg(sendMessage, update);
				controller.setUserState(id, 4);
			} else {
				sendMessage.setText("Я таких команд не знаю");
				sendMsg(sendMessage, update);
				return;
			}

			break;
		case 2:
			String goodName = update.getMessage().getText().split(",")[0];
			int goodPrice = Integer.parseInt(update.getMessage().getText().split(",")[1].substring(1,
					update.getMessage().getText().split(",")[1].length() - 5));
			int goodId = controller.getGoodId(goodName, goodPrice);
			if (goodId == -1) {
				sendMessage.setText("У меня такого нету");
				sendMsg(sendMessage, update);
				return;
			}
			controller.setLastChoice(goodId, id);
			if (controller.hasContact(id)) {
				sendMessage.setText("Ваш контакт уже записан, хотите обновить?");
				setChoiceButtons(sendMessage);
				sendMsg(sendMessage, update);
				controller.setUserState(id, 5);

			} else {
				sendMessage.setText("Дайте теперь свой контакт, пожалуйста (кнопка внизу)");
				setShareContactButtons(sendMessage);
				sendMsg(sendMessage, update);
				controller.setUserState(id, 6);
			}

			break;
		case 3:
			int goodid = controller.getLastChoice(id);
			DBUser u = controller.getUser(id);
			boolean result = controller.saveOrder(id, goodid);
			if (result) {
				setOrderAgainButtons(sendMessage);
				Product p = controller.getAllGoods().get(goodid);
				sendMessage.setText("Заказ записан, ожидайте сообщение от админа");
				sendMsg(sendMessage, update);
				SendMessage adminMsg = new SendMessage();
				adminMsg.setChatId(controller.getAdminChatId());
				adminMsg.setText("New order by: " + "Contact= " + u.getNumber() + ", First name= " + u.getName()
						+ ", Username= " + u.getUsername() + " | Product: " + p.getName() + " for " + p.getPrice());
				sendMsg(adminMsg, update);
				controller.incrementUserState(id);

			} else {
				sendMessage.setText("Что-то пошло не так (");
				sendMsg(sendMessage, update);
			}

			break;
		case 4:
			String input = update.getMessage().getText();
			String ownerid = controller.getPromoOwner(input);
			
			if(message.toLowerCase().trim().equals("выйти")) {
				sendMessage.setText("Что делаем?");
				setMenuButtons(sendMessage);
				sendMsg(sendMessage, update);
				controller.setUserState(id, 1);
				return;
			}
			
			if (ownerid == null) {
				sendMessage.setText("Такого промокода я не знаю.");
				setOrderAgainButtons(sendMessage);
				sendMsg(sendMessage, update);
				controller.setUserState(id, 0);
				return;
			}
			if (ownerid.equals(id)) {
				sendMessage.setText("Вы не можете активировать свой промокод");
				setOrderAgainButtons(sendMessage);
				sendMsg(sendMessage, update);
				controller.setUserState(id, 0);
				return;
			}

			
			System.out.println(controller.usedPromo(id));
			System.out.println(controller.hasOrders(id));
			
			if (controller.usedPromo(id) || controller.hasOrders(id)) {
				sendMessage.setText("Вы не можете активировать промокод так как Вы не новый пользователь.");
				setOrderAgainButtons(sendMessage);
				sendMsg(sendMessage, update);
				controller.setUserState(id, 0);
				return;
			}

			boolean promoResult = controller.usePromo(input);
			if (promoResult) {

				sendMessage.setText("Промокод активирован");
				setOrderAgainButtons(sendMessage);
				sendMsg(sendMessage, update);
				SendMessage ownerConfirmation = new SendMessage();
				ownerConfirmation.setChatId(ownerid);
				ownerConfirmation.setText("Вашим промокодом " + input
						+ " кто-то воспользовался. \n Это сообщение - гарантия получения второго стекла бесплатно за приглашенного друга. \n Покажите это сообщение админу при уточнениии заказа.");
				sendMsg(ownerConfirmation, update);
				SendMessage adminConfirmation = new SendMessage();
				adminConfirmation.setChatId(controller.getAdminChatId());
				adminConfirmation.setText("Promo " + input + " of " + ownerid + " was used by firstname= "
						+ update.getMessage().getFrom().getFirstName() + ", username= "
						+ update.getMessage().getFrom().getUserName() + " at " + LocalDate.now() + " "
						+ LocalTime.now());
				sendMsg(adminConfirmation, update);
				controller.setUserState(id, 0);
			} else {
				sendMessage.setText("Что-то пошло не так (");
				sendMsg(sendMessage, update);
			}
			break;
		case 5:
			if (message.toLowerCase().equals("да")) {
				sendMessage.setText("Дайте теперь свой контакт, пожалуйста (кнопка внизу)");
				setShareContactButtons(sendMessage);
				sendMsg(sendMessage, update);
				controller.setUserState(id, 6);
			} else if (message.toLowerCase().equals("нет")) {
				sendMessage.setText("Oк, нажмите кнопку чтобы закончить заказ");
				removeButtons(sendMessage);
				sendMsg(sendMessage, update);
				controller.setUserState(id, 3);
			} else {
				sendMessage.setText("Я таких команд не знаю");
				sendMsg(sendMessage, update);
				return;
			}
			break;
		case 6:
			String firstName1 = update.getMessage().getContact().getFirstName();
			String lastName1 = update.getMessage().getContact().getLastName();
			String contact1 = update.getMessage().getContact().getPhoneNumber();
			controller.updateUser(id, firstName1, lastName1, contact1, controller.getLastChoice(id));
			controller.setUserState(id, 3);
			removeButtons(sendMessage);
			sendMessage.setText("Нажмите кнопку чтобы закончить заказ");
			sendMsg(sendMessage, update);

			break;

		}

	}

	public synchronized void sendMsg(SendMessage sendMessage, Update update) {

		try {
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {

		}
	}

	public String getBotUsername() {
		return name;
	}

	public String getBotToken() {
		return token;
	}

	public synchronized void setMenuButtons(SendMessage sendMessage) {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardButton b1 = new KeyboardButton("Каталог");
		KeyboardButton b2 = new KeyboardButton("Мои заказы");
		KeyboardButton b3 = new KeyboardButton("Мой промокод");
		KeyboardButton b4 = new KeyboardButton("Ввести промокод");

		KeyboardRow keyboardFirstRow = new KeyboardRow();
		keyboardFirstRow.add(b1);

		KeyboardRow keyboardSecondRow = new KeyboardRow();
		keyboardSecondRow.add(b2);

		KeyboardRow keyboardThirdRow = new KeyboardRow();
		keyboardThirdRow.add(b3);
		keyboardThirdRow.add(b4);

		keyboard.add(keyboardFirstRow);
		keyboard.add(keyboardSecondRow);
		keyboard.add(keyboardThirdRow);

		replyKeyboardMarkup.setKeyboard(keyboard);
	}

	public synchronized void setProductButtons(SendMessage sendMessage, List<Product> goods) {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);
		int buttonsPerRow = 3;
		List<KeyboardRow> keyboard = new ArrayList<>();
		
		int rows = goods.size() / buttonsPerRow + 1;
		for (int i = 0; i < rows; i++) {
			KeyboardRow keyboardRow = new KeyboardRow();
			for (int j = 0; j < 3; j++) {
				keyboardRow.add(
						new KeyboardButton(goods.get(j+i*(buttonsPerRow-1)).getName() + ", " + goods.get(j+i*(buttonsPerRow-1)).getPrice() + " грн."));
				
			}
			keyboard.add(keyboardRow);

		}
		replyKeyboardMarkup.setKeyboard(keyboard);
	}

	public synchronized void setShareContactButtons(SendMessage sendMessage) {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardButton b1 = new KeyboardButton("Нажмите чтобы дать свой контакт");
		b1.setRequestContact(true);

		KeyboardRow keyboardFirstRow = new KeyboardRow();
		keyboardFirstRow.add(b1);

		keyboard.add(keyboardFirstRow);
		replyKeyboardMarkup.setKeyboard(keyboard);
	}

	public synchronized void setOrderAgainButtons(SendMessage sendMessage) {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardButton b1 = new KeyboardButton("Начать работу");

		KeyboardRow keyboardFirstRow = new KeyboardRow();
		keyboardFirstRow.add(b1);

		keyboard.add(keyboardFirstRow);
		replyKeyboardMarkup.setKeyboard(keyboard);
	}

	public synchronized void setChoiceButtons(SendMessage sendMessage) {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardButton b1 = new KeyboardButton("Да");
		KeyboardButton b2 = new KeyboardButton("Нет");

		KeyboardRow keyboardFirstRow = new KeyboardRow();
		keyboardFirstRow.add(b1);
		keyboardFirstRow.add(b2);

		keyboard.add(keyboardFirstRow);
		replyKeyboardMarkup.setKeyboard(keyboard);
	}

	public synchronized void quitButtons(SendMessage sendMessage) {

		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);
		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardButton b1 = new KeyboardButton("Выйти");
		KeyboardRow keyboardFirstRow = new KeyboardRow();
		keyboardFirstRow.add(b1);
		keyboard.add(keyboardFirstRow);
		replyKeyboardMarkup.setKeyboard(keyboard);
	}

	public synchronized void removeButtons(SendMessage sendMessage) {

		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);
		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardButton b1 = new KeyboardButton("Закончить заказ");
		KeyboardRow keyboardFirstRow = new KeyboardRow();
		keyboardFirstRow.add(b1);
		keyboard.add(keyboardFirstRow);
		replyKeyboardMarkup.setKeyboard(keyboard);
	}
}
