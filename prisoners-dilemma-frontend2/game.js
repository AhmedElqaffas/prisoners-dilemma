import { Client } from "@stomp/stompjs";

var client;

$(document).ready(async function () {
  const gameId = getGameId();
  if (!gameId) {
    return;
  }
  connect(gameId);
  setButtonsListeners();
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
  client = new Client({
    brokerURL: "ws://localhost:8080/game-connection-ws",
    onConnect: () => {
      client.subscribe(`/topic/game/${gameId}`, function (message) {
        console.log(message.body);
      });
      client.subscribe("/user/queue/player", (meessage) => {
        console.log(meessage.body);
      });
    },
  });

  client.activate();
}

function setButtonsListeners() {
  $("#split").on("click", function () {
    split();
  });
}

function split() {
  client.publish({
    destination: `/app/topic/game/${getGameId()}`,
    body: JSON.stringify({ choice: "SPLIT" }),
  });
}
