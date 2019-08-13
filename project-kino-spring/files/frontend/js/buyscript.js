function buy(){

var data = sessionStorage.getItem("data");
var buy = new XMLHttpRequest();
buy.open("POST", "http://127.0.0.1:4567/bui", true);
buy.withCredentials = true;
buy.send(data);
window.location.href = "user.html";

}
