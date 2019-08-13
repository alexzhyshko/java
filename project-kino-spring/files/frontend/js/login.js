function login(){
  var http = new XMLHttpRequest();
  http.open("POST", "http://127.0.0.1:4567/login");
  http.withCredentials = true;
  if($('#usrnm').val().trim()===''||$('#pswrd').val().trim()===''){
    $('#error')[0].innerHTML = 'Empty credentials';
  }else{

  var str = $('#usrnm').val().trim()+","+$('#pswrd').val().trim();
  http.send(str);
  http.onreadystatechange = function(){
    if(http.readyState == 4 && http.status==200){
      console.log(http.response);
      window.location.href=http.response;
    }
    if(http.readyState == 4 && http.status==400){
      $('#error')[0].innerHTML = http.response;
    }

  };
}
};
