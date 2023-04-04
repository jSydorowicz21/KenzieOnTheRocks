import { ListViewComponent } from '@syncfusion/ej2-react-lists';
import { ButtonComponent } from '@syncfusion/ej2-react-buttons';
import React from "react";

function createDropDown(list, id) {
    const alcohols = ["rum", "vodka", "gin", "tequila", "whiskey", "brandy", "liqueur", "beer", "wine", "cider", "sake", "shochu", "mead", "vermouth", "absinthe", "bitters", "cordial", "aperitif", "cocktail", "schnapps", "champagne"];
    const mixers = ["apple juice", "orange juice", "grape juice", "pineapple juice", "mango juice", "peach juice", "tomato juice", "cranberry juice", "grapefruit juice", "lemon juice", "lime juice", "coconut water", "coconut milk", "ginger ale", "ginger beer", "soda water", "club soda", "tonic water", "seltzer water", "sparkling water", "coffee", "tea", "milk", "water"]
    const finishers = ["sugar syrup", "simple syrup", "grenadine", "egg white", "cream", "ice", "salt", "pepper", "sugar", "cinnamon", "nutmeg", "clove", "cayenne pepper", "vanilla", "chocolate", "cocoa", "caramel", "honey", "maple syrup", "lemon", "lime", "orange", "grapefruit", "apple", "banana", "pineapple", "mango", "peach", "strawberry", "raspberry", "blueberry", "blackberry", "cherry", "pomegranate", "kiwi", "avocado", "tomato", "basil", "mint", "rosemary", "thyme", "oregano", "cilantro", "garlic", "onion", "ginger", "lime", "lemon", "orange", "grapefruit", "apple", "banana", "pineapple", "mango", "peach", "strawberry", "raspberry", "blueberry", "blackberry", "cherry", "pomegranate", "kiwi", "avocado", "tomato", "basil", "mint", "rosemary", "thyme", "oregano", "cilantro", "garlic", "onion", "ginger", "lime", "lemon", "orange", "grapefruit", "apple", "banana", "pineapple", "mango", "peach", "strawberry", "raspberry", "blueberry", "blackberry", "cherry", "pomegranate", "kiwi", "avocado", "tomato"]
    let listobj = null;

    const [state, SetState] = React.useState({
        selectedItemsValue: []
    });
    function getSelectedItems() {
        if (listobj) {
            SetState({
                selectedItemsValue: listobj.getSelectedItems().data
            });
        }
    }
    return (<div>
        <ListViewComponent id={id} dataSource={list} showCheckBox={true} ref={scope => {
            listobj = scope;
        }}/>
        <ButtonComponent id="btn" onClick={getSelectedItems.bind(this)}>
            Get Selected Items
        </ButtonComponent>
        <div>
            <table>
                <tbody>
                <tr>
                    <th>Text</th>
                    <th>Id</th>
                </tr>

                {state.selectedItemsValue.map((item, index) => {
                    return (<tr key={index}>
                        <td>{item}</td>
                        <td>{item}</td>
                    </tr>);
                })}
                </tbody>
            </table>
        </div>
    </div>);
}

function getAndRenderDrinks(root, drinks, className){
    const drinkCardArr = [];

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

export {renderDrinkMenu, getAndRenderDrinks};