function hallrender(){


  var rws=9;
  var columns=15;
  var price = 0;

//get info
var http = new XMLHttpRequest();
http.open("GET", "http://127.0.0.1:4567/loadhall", true);
http.withCredentials = true;
http.send();
http.onreadystatechange = function(){
  if(http.readyState == 4 && http.status==200){
  var places = http.response;
    rows = places.split("|")[1].split(";");
    price = parseInt(places.split("|")[0]);
    $('#cheap')[0].innerHTML = price+"&#8372;";
    $('#middle')[0].innerHTML = price*1.2+"&#8372;";
    $('#expensive')[0].innerHTML = price*1.6+"&#8372;";
    for(var i=0; i<rows[0].length;i++){

      var row = rows[i];
      for(var j=0;j<row.length;j++){
        var col = row.split(',')[j];
        if(col==2){
          $('#'+(i+1)+'-'+(j+1)+'').addClass('taken');
        }
        if(col==1){
          $('#'+(i+1)+'-'+(j+1)+'').addClass('booked');
        }

      }
    }


  }

};

rws;
columns;

var http1 = new XMLHttpRequest();
http1.open("GET", "http://127.0.0.1:4567/gettitle", true);
http1.withCredentials = true;
http1.send();
http1.onreadystatechange = function(){
  if(http1.readyState == 4 && http1.status==200){
    $('#undertext')[0].innerHTML = http1.response;
  }
};








var choice = [];

var start = parseInt(columns/2);
for(var i=1; i<rws+1;i++){
  $('#plan')[0].innerHTML += '  <div class="row" id="row'+i+'">'
  if(start>0){
    start-=2;
  }
  for(var j=1; j<columns-start; j++){

    $('#plan')[0].innerHTML+='<input type="text" id="'+i+'-'+j+'" class="clickable" name="choice" value="'+i+'-'+j+'">';
  }

  $('#plan')[0].innerHTML+='</div>';
}

var overallPrice=0;
$('#overallprice')[0].innerText = 0;
$('.clickable').click(function(){
  var elem = $(this);







  if(!($(this).attr('class').includes('taken'))&&!($(this).attr('class').includes('booked'))){

  if($(this).attr('class').includes('selected')){
    if(parseInt(elem.attr('value').split('-')[0])<3){
      overallPrice-=price;
    }else if (parseInt(elem.attr('value').split('-')[0])>=3&&parseInt(elem.attr('value').split('-')[0])<7) {
      overallPrice-=price*1.2;
    }else{
      overallPrice-=price*1.6;
    }
    $('#overallprice')[0].innerText = overallPrice;
    elem.removeClass('selected');
    choice = choice.filter(function(value, index, arr){
      return value!=elem.attr('value');
  });
}else{

  if(parseInt(elem.attr('value').split('-')[0])<3){
    overallPrice+=price;
  }else if (parseInt(elem.attr('value').split('-')[0])>=3&&parseInt(elem.attr('value').split('-')[0])<7) {
    overallPrice+=price*1.2;
  }else{
    overallPrice+=price*1.6;
  }
$('#overallprice')[0].innerText = overallPrice;
  elem.addClass('selected');
    choice.push(elem.attr('value'));
  }
  $('#list')[0].innerHTML='';
  for(var i=0; i<choice.length; i++){
    $('#list')[0].innerHTML+='<li>'+choice[i]+'</li>';
  }
}



});



$('#submit').click(function(){
  if(choice.length!=0){
  var http2 = new XMLHttpRequest();
  http2.open("POST", "http://127.0.0.1:4567/book", true);
  http2.withCredentials = true;
  var res = "";
  for(var k=0;k<choice.length;k++){
    res+=choice[k]+",";
  }


  res = res.substring(0,res.length-1);

  http2.send(res);
  http2.onreadystatechange = function(){
    if(http2.readyState == 4 && http2.status==300){
      window.location.href = http2.response;
    }
    if(http2.readyState == 4 && http2.status==400){
      alert("These tickets were already bought by another user.\n Please, choose other.\n Sorry for inconvenience");
      $.each(choice, function(i, item) {
        $('#'+item).removeClass('selected');
      });
      choice=[];
      $('#list')[0].innerHTML='';



    }
  };
}

});

};
