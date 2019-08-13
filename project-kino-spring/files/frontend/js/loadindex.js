function loadindex(){
  var http = new XMLHttpRequest();
  http.open("GET", "http://127.0.0.1:4567/loadindex", true);
  http.send();
  http.onreadystatechange = function(){
    if(http.readyState == 4 && http.status==200){
      var soon = Array(5);
      var offers = Array(3);
      soon = http.response.split(";")[0].split(",").slice(0,5);
      offers = http.response.split(";")[1].split(",").slice(0,3);
      var items = [];
      $.each(soon, function(i, item) {
        items.push('<li>'+item+'</li>');
      });
      $('#soonUl').append(items.join(''));
      items = [];
      $.each(offers, function(i, item) {
        items.push('<li>'+item+'</li>');
      });
      items.push('<li class="special-li">More</li>');
      $('#offerUl').append(items.join(''));
      }
  };
};
