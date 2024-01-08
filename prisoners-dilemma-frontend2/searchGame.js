$(document).ready(async function () {
  const gameId = await search();
  if (!gameId) {
    alert("You are already in a game");
    return;
  }

  document.location.href =
    "http://localhost:3000/game?game=" + encodeURIComponent(gameId);
});

async function search() {
  const response = await fetch("http://localhost:8080/connection/search", {
    method: "POST",
    headers: {
      Accept: "application/json",
    },
    redirect: "follow",
    credentials: "include",
  });

  if (response.redirected) {
    document.location = "http://localhost:3000/login";
  } else {
    return await response.text();
  }
  return gameId;
}
