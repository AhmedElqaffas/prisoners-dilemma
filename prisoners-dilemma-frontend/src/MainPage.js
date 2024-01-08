import { search } from "./searchGame";

function MainPage() {
  return (
    <main>
      <button onClick={search}>Search for Game</button>
    </main>
  );
}

export default MainPage;
