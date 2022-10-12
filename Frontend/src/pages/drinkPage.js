import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import DrinkClient from "../api/drinkClient";
import UserClient from "../api/userClient";
/**
 * Logic needed for the view playlist page of the website.
 */
class DrinkPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['update', 'delete', 'addToList'], this);
        this.dataStore = new DataStore();
        this.drinkClient = new DrinkClient();
        this.userClient = new UserClient();

    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the restaurant/review list.
     */
    mount() {


        let drinkName = sessionStorage.getItem("drinkName");
        let ingredients = sessionStorage.getItem("ingredients");

        document.getElementById("card").innerHTML = `
                <h1>Drink Name</h1>
                <h1>${drinkName}</h1>
                <p><label>Ingredients</label></p>
                <div id="drinkvalues">${ingredients}</div>
                <button id="update"> Edit</button>
                <button id="delete">Delete</button>
                <button id="add">Add to my list</button>`
        document.getElementById('update').addEventListener('click', this.edit);
        document.getElementById('delete').addEventListener('click', this.delete);
        document.getElementById('add').addEventListener('click', this.addToList);

    }

    async update(event) {
        event.preventDefault();

        let userId = sessionStorage.getItem('userId');
        let drinkId = sessionStorage.getItem('drinkId');
        let drinkName = sessionStorage.getItem('drinkName');
        let ingredients = sessionStorage.getItem('ingredients');

        let result = await this.drinkClient.updateDrink(drinkId, drinkName, ingredients, userId);

        if(result.status === 200) {
            this.showMessage('Drink updated successfully');
        }
        else {
            this.errorHandler("Error updating drink");
        }

    }

    async delete(event) {
        event.preventDefault();

        let drinkId = sessionStorage.getItem('drinkId');

        let result = await this.drinkClient.deleteDrink(drinkId);

        if(result.status === 200) {
            this.showMessage('Drink deleted successfully');
        }
        else {
            this.errorHandler("Error deleting drink");
        }

    }

    async addToList(event) {
        event.preventDefault();

        let userId = sessionStorage.getItem('userId');
        let drinkId = sessionStorage.getItem('drinkId');
        let drinkName = sessionStorage.getItem('drinkName');
        let ingredients = sessionStorage.getItem('ingredients');

        var drink = {
            userId: userId,
            drinkId: drinkId,
            drinkName: drinkName,
            ingredients: ingredients
        }


        let result = this.userClient.addToList(userId, drink);

        if(result) {
            console.log("Drink added");
        }
        else {
            this.errorHandler("Error adding drink to list!  Try again...");
        }

    }

}
/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const drinkPage = new DrinkPage();
    if (sessionStorage.getItem("drinkId") == null){
        window.location.href = "index.html";
    }
    drinkPage.mount();

};

window.addEventListener('DOMContentLoaded', main);