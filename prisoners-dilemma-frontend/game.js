$(document).ready(async function () {
  const gameId = getGameId();
  if (!gameId) {
    return;
  }
  connect(gameId);
});

function getGameId() {
  const url = document.location.href;
  const params = url.split("?")[1];
  if (!params) {
    return null;
  }
  const gameId = params.split("=")[1];
  return gameId;
}

function connect(gameId) {
  var socket = new WebSocket("ws://localhost:8080/game-connection-ws");
  var stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    stompClient.subscribe(`/topic/game/${gameId}`, function (message) {
      console.log(message);
    });
  });
}

/*function connect() {
  //var socket = new SockJS("http://localhost:8080/game-connection-ws");
  var socket = new WebSocket("ws://localhost:8080/game-connection-ws");
  var stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log("Connected: " + frame);

    var response = stompClient.send("/app/search", {}, "");

    console.log(response);

    stompClient.subscribe("/user/topic/${}", function (message) {
      console.log(message);
    });
  });
}*/
