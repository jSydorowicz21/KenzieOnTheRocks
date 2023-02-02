export function otherHelperMethod(){

}
export default function getAndRenderDrinks(root, drinks, className){
    let drinkCardArr = [];

    const drinkHandler = (drink) => {
        const clickHandler = () => {
            sessionStorage.setItem("drinkId", drink.id);
            sessionStorage.setItem("drinkName", drink.name);
            sessionStorage.setItem("ingredients", drink.ingredients);
            window.location.href = "/drink.html";
        }

        return (<div class = {className} onClick = {clickHandler}>
                <h4><b>Drink Name: {drink.name}</b></h4>
                <p>Ingredients: {drink.ingredients}</p>
                </div>);
    }

    drinks.forEach(function (drink) {
            drinkCardArr.push(drinkHandler(drink));
    });
    root.render(drinkCardArr);
}