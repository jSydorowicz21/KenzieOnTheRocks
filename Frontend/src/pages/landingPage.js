import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import DrinkClient from "../api/drinkClient";
import UserClient from "../api/userClient";
import * as bootstrap from 'bootstrap';

/**
 * Logic needed for the view playlist page of the website.
 */
class LandingPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onGetAllDrinks', 'onCreate',  'onGetUserDrinks'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('searchButton').addEventListener('click', this.onGet);
        document.getElementById('createButton').addEventListener('click', this.onCreate);
        document.getElementById('homeButton').addEventListener('click', this.onGetAllDrinks);
        this.client = new DrinkClient();
        this.userClient = new UserClient();

        await this.onGetAllDrinks();
        await this.onGetUserDrinks();

    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let ingredients = document.getElementById("ingredient-string").value;
        //this.dataStore.set("drinks", null);

        let result = await this.client.getFilteredDrink(ingredients, this.errorHandler);
        this.dataStore.set("drinks", result);
        if (result) {
            this.showMessage(`Got ${result.name}!`)

        } else {
            this.errorHandler("Error doing GET!  Try again...");


        }
        document.querySelector('input[name="filter"]:checked').checked = false;

    }

    async onGetAllDrinks() {

        let result = await this.client.getHomeDrinks();
        let drinks = Array.from(result);
        document.getElementById("result").innerHTML = "";
        if (result) {
            var htmlToinsert = "";

            drinks.forEach(function (drink) {
                htmlToinsert += `<div class="drink">
                <h4><b>Drink Name: ${drink.name}</b></h4>
                <p>Ingredients: ${drink.ingredients}</p>
            </div>`
            });
            document.getElementById("result").innerHTML = htmlToinsert;

            var drinkCards = Array.from(document.getElementsByClassName('drink'));

            drinkCards.forEach(function (drinkCard, index) {
                    drinkCard.addEventListener('click', function () {
                        sessionStorage.setItem("drinkId", drinks[index].id);
                        sessionStorage.setItem("drinkName", drinks[index].name);
                        sessionStorage.setItem("ingredients", drinks[index].ingredients);
                        window.location.href = "/drink.html";
                    });
                });

        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }

    }

    async onGetUserDrinks() {
        console.log(sessionStorage.getItem("userId"));
        let drinks = await this.userClient.getUsersDrinks(sessionStorage.getItem("userId"));

        if (drinks == null){
            document.getElementById("sidebar").innerHTML = "";
        }
        else {
            var htmlToinsert = "";

            drinks.forEach(function (drink) {
                htmlToinsert += `<div class="side-drink">
                <h4><b>Drink Name: ${drink.name}</b></h4>
                <p>Ingredients: ${drink.ingredients}</p>
            </div>`
            });
            document.getElementById("sidebar").innerHTML = htmlToinsert;

            var drinkCards = Array.from(document.getElementsByClassName('side-drink'));

            drinkCards.forEach(function (drinkCard, index) {
                drinkCard.addEventListener('click', function () {
                    sessionStorage.setItem("drinkId", drinks[index].id);
                    sessionStorage.setItem("drinkName", drinks[index].name);
                    sessionStorage.setItem("ingredients", drinks[index].ingredients);
                    window.location.href = "/drink.html";
                });
            });
        }

    }

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("drink", null);

        let name = document.getElementById("create-name-field").value;

        let userId = sessionStorage.getItem("userId");

        let ingredients = document.querySelectorAll('input:checked');

        let ingredientsArray = [];

        for (let i = 0; i < ingredients.length; i++) {
            ingredientsArray = Array.from(ingredients).map(ingredient => String(" " + ingredient.value));
        }

        const createdDrink = await this.client.createDrink(userId, name, ingredientsArray, this.errorHandler);
        this.dataStore.set("drink", createdDrink);

        if (createdDrink) {
            this.showMessage(`Created ${createdDrink.name}!`)

        } else {
            this.errorHandler("Error creating!  Try again...");
        }

        await this.onGetAllDrinks();
    }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const landingPage = new LandingPage();

    if (sessionStorage.getItem("userId") == null) {
        window.location.href = "login.html";
    }

    if (sessionStorage.getItem("drinkId") != null){
        sessionStorage.removeItem("drinkId");
        sessionStorage.removeItem("drinkName");
        sessionStorage.removeItem("ingredients");
    }

    await landingPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
