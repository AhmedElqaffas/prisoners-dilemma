export async function search() {
  const response = await fetch("http://localhost:8080/connection/search", {
    method: "POST",
    headers: {
      Accept: "application/json",
    },
    redirect: "follow",
    credentials: "include",
  }).catch((e) => console.log(e));

  if (!response) {
    return;
  }
  if (response.redirected) {
    document.location = "http://localhost:3001/login";
  } else {
    const gameId = await response.text();
    if (!gameId) {
      alert("You are already in a game");
      return;
    }

    document.location.href =
      "http://localhost:3001/game?game=" + encodeURIComponent(gameId);
  }
}
