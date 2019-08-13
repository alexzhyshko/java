function loadtoday(){
  var http = new XMLHttpRequest();
  http.open("GET", "http://127.0.0.1:4567/loadtoday", true);
  http.withCredentials = true;
  http.send();
  http.onreadystatechange = function(){
    if(http.readyState == 4 && http.status==200){
      var response = http.response;
      var films = response.split(";");
      if(response.trim()==''){
        $('#undertext')[0].innerHTML = 'No more for today :(';
      }else{
      var items = [];
      $.each(films, function(i, item) {
        var title = item.split(",")[0];
        var time = item.split(",")[1];
        var hall = item.split(",")[2];
        items.push('<li>'+
          '<ul class="innerUl">'+
            '<li><h1>'+title+'</h1></li>'+
            '<li>'+time+'</li>'+
            '<li>    "'+hall+'" Hall</li>'+
          '</ul>'+
        '</li>');
      });
      $('#suggestionsUl').append(items.join(''));
    }
  }
    handleclick();
  };
};

function handleclick(){
  $(".innerUl").click(function() {
    var obj = $(this).children();
    var title = obj[0].innerHTML.substring(4,obj[0].innerHTML.length-5);
    var time = obj[1].innerHTML.trim();
    var hall = obj[2].innerHTML.trim().substring(1, obj[2].innerHTML.trim().length-6);
    var info = title+","+time+","+hall;
    var http = new XMLHttpRequest();
    http.open("POST", "http://127.0.0.1:4567/loadseanse", true);
    http.withCredentials = true;
    http.onreadystatechange = function(){
      if(http.readyState == 4 && http.status==300){
        window.location.href=http.response;
      }
    };
    http.withCredentials = true;
    http.send(info);
  });
};
