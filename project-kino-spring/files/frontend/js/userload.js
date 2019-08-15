
function get(){
  var req = new XMLHttpRequest();
  req.open("GET", "http://127.0.0.1:4567/loaduser", true);
  req.withCredentials = true;
  req.send();
  req.onload = reqListener;
}


function reqListener(){



  if(this.readyState==4){
    if(this.responseText=='login.html'){

      window.location.href = 'login.html';

    }else{

      var txt = this.responseText;

      var username = txt.split(";")[0];
      var tickets = txt.split(";")[1];
      if(tickets==undefined){
        $('#username')[0].innerHTML = username;
        $('#undertext')[0].innerHTML = 'You have no tickets';
      }else{
      var ticketsPerSeanse = tickets.split("_");

      $.each(ticketsPerSeanse, function(i, item) {
        var items = [];
        var seanse = item.split("/")[0];
        var title = seanse.split("+")[0];
        var hall = title.split(',')[1];
        hall = hall.substring(2,hall.length-6);
        var price = parseInt(title.split(",")[3]);
        var time = seanse.split("+")[1];
        var status = title.split(",")[2];
        var date = time.split(",")[1];
        var timee = time.split(",")[0];
        timee = timee.substring(1,timee.length-3);
        var month = date.split("-")[0];
        var day = date.split("-")[1];
        var date = day.substring(0,day.length-1)+"."+month.substring(1,day.length);

        title = title.split(",")[0]+", "+title.split(",")[1];


        time = timee+ " "+ date;
        var tickets = item.split("/")[1];
        var splittedTickets = tickets.split(",");
        var overallPrice = 0;

        for(var i=0; i<splittedTickets.length;i++){
          if(parseInt(splittedTickets[i].split("-")[0])<3){
            overallPrice+=price;

          }else if (parseInt(splittedTickets[i].split("-")[0])>=3&&parseInt(splittedTickets[i].split("-")[0])<7) {
            overallPrice+=price*1.2;

          }else{
            overallPrice+=price*1.6;

          }
        }
        overallPrice-=160;
        var push = '<li class="ticketList">'+
        '<ul class="innerUl">'+
        '  <li class="title-li">'+title+'</li>';
          if(status.trim()=='BOOKED'){
            push+='  <li class="time-date-li">'+timee+' '+date+' ('+overallPrice+'&#8372;)</li>';
            push+='  <li class="status-li">'+status+', CLICK TO BUY</li>';
          }else{
            push+='<li class="time-date-li">'+timee+' '+date+'</li>';
          }
        push+='  <li class="tickets">'+
          '  <ul class="ticketUl">';

            for(var i=0; i<splittedTickets.length;i++){
              push+='<li class="ticket" style="background:'+hall+'">'+splittedTickets[i].trim()+'</li>';
            }
            push = push.substring(0,push.length-25);
            push+='</ul></li></ul></li>';

        $('#ticketsUl').append(push);
      });

      $('#username')[0].innerHTML = username;
    }
  }
  }

check();

}

  function check(){


  $('.innerUl').on("click",function(){
    var elem = $(this).children();
    var title = elem[0].innerText.split(",")[0];
    var hall = elem[0].innerText.split(",")[1];
    hall = hall.substring(2,hall.length-6);
    var datetime = elem[1].innerText;
    var status = elem[2].innerText;
    var places="";
    var pl = places = elem.children().children();
    for(var i=0; i<pl.length; i++){
      places+=pl[i].innerText+" ";
    }

    places = places.substring(15);

    if(status.trim()=='BOOKED, CLICK TO BUY'){
      sessionStorage.setItem("data", places+";"+title+","+datetime.split(" ")[1]+","+datetime.split(" ")[0]+","+hall);
      window.location.href = 'buy.html';
    }

  });


  $('#logout').click(function(){
    var logout = new XMLHttpRequest();
    logout.open("GET", "http://127.0.0.1:4567/logout");
    logout.withCredentials = true;
    logout.send();
    logout.onreadystatechange = function(){
      if(logout.readyState == 4&& logout.status==200){
        window.location.href = "login.html";
      }
    };
  });
}
