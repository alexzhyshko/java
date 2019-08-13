function loadfilms(){

  var http = new XMLHttpRequest();
  http.open("GET", "http://localhost:4567/loadfilms",true);
  http.withCredentials = true;
  http.send();
  http.onreadystatechange = function(){
    if(http.readyState==4&&http.status==200){
      if(http.responseText.trim()==''){
        $('#list')[0].innerHTML = 'No more seances left :(';
      }else{
      var resp = http.responseText.split(";");
      console.log(http.responseText);
      for(var i=0;i<resp.length;i++){
        var title = resp[i].split("+")[0];
        var data = resp[i].split("+")[1];

        var days = data.split("/");
        var append= '<li class="inner-li"><ul id="inner-list"><a class="list-title">'+title+'</a>';
        for(var j=0;j<days.length;j++){

          var date = days[j].split("-")[0];
          var time = days[j].split("-")[1].split("_");

          //sort start
          for(var k=0;k<time.length;k++){
            time[k] = time[k].substring(0,2)+":"+time[k].substring(3,11).trim();
          }

console.log(time);

          time.sort((a, b) => a - b);
          append +='<ul class="inner-inner-ul" id="inner-inner-ul1">'+
            '<li class="date">'+date+'</li>'+
            '<li>'+
            '<ul class="time-list" id="time-list-1">';
          for(var k=0;k<time.length;k++){
            var color = time[k].split(" ")[1];

            var timespl = time[k].split(" ")[0];
            append+='<li id="'+color+'" style="background:'+color+'">'+timespl+'</li>';
            time[k] = time;
          }
          append+='</ul>'+
        '</li>'+
      '</ul>';
          //sort end


            for(var k=0; k<time.length;k++){

            }







        }
$('#list').append(append);
      }



    }
  }
  $(".time-list li").click(function(){
    var time=$(this)[0].innerHTML;
    var date = $(this).parent().parent().parent().find("li:nth-child(1)")[0].innerHTML;
    var title = $(this).parent().parent().parent().parent().find(".list-title")[0].innerHTML;
    var color=$(this).attr("id");
    var http = new XMLHttpRequest();
    http.open("POST", "http://127.0.0.1:4567/loadhallfilms", true);
    http.withCredentials = true;
    http.send(title+","+date+"-2019"+","+time+","+color);
    console.log("sent");
    console.log(http.response);
    http.onreadystatechange = function(){
      if(http.readyState==4){
      window.location.href=http.response;
    }
  };

  });
  };




};
