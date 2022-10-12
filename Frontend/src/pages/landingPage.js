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
        this.bindClassMethods(['onGet', 'onGetAllDrinks', 'onCreate', 'renderDrink' , 'onGetUserDrinks', 'storeShit'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('searchButton').addEventListener('click', this.onGet);
        //document.querySelector("[name=filter]:checked").addEventListener('click', this.onGet);
        document.getElementById('createButton').addEventListener('click', this.onCreate);
        document.getElementById('homeButton').addEventListener('click', this.onGetAllDrinks);
        this.client = new DrinkClient();
        this.userClient = new UserClient();

        this.dataStore.addChangeListener(this.renderDrink)
        await this.onGetAllDrinks();
        await this.onGetUserDrinks();


    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderDrink() {
        let resultArea = Array.from(document.getElementsByClassName('drink'));

        const drink = this.dataStore.get("drink");


        if (drink) {
            resultArea[1].innerHTML = `
                <h4><b>Drink Name: ${drink.name}</b></h4>
			    <p>Ingredients: ${drink.ingredients}</p>
            `
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let filter = document.querySelector("[name=filter]:checked").value;
        let ingredients = document.getElementById("searchFilter").value;
//        this.dataStore.set("drinks", null);

        let result = await this.client.getDrink(id, this.errorHandler);
        this.dataStore.set("drinks", result);
        if (result) {
            this.showMessage(`Got ${result.name}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onGetAllDrinks() {

        let result = await this.client.getHomeDrinks();
        let drinks = Array.from(result);
        if (result) {
            console.log("step 1");
            document.getElementById("result").innerHTML = `
        `
            for (let i = 0; i < drinks.length; i++) {
                let drink = drinks[i];
                ` <div class="drink" id="drink${i.toString()}">
                <h4><b>Drink Name: ${drink.name}</b></h4>
                <p>Ingredients: ${drink.ingredients}</p>
            </div>
            `
                document.getElementById("drink" + i.toString()).addEventListener('click', await this.storeShit(drink.id, drink.name, drink.ingredients));
            }
            `
        `
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }

    }

    async onGetUserDrinks() {
        let drinks = this.userClient.getUsersDrinks(sessionStorage.getItem("userId"));

        document.getElementById("sidebar").innerHTML = `
        `
        for (let i = 0; i < drinks.length; i++) {
           let drink = drinks[i];
           ` <div class="side-drink" id="${i}">
                <h4><b>Drink Name: ${drink.name}</b></h4>
                <p>Ingredients: ${drink.ingredients}</p>
            </div>
            `
            document.getElementById(i.toString()).addEventListener('click', await this.storeShit(drink.id, drink.name, drink.ingredients));
        }
        `
        `
    }

    async storeShit(id, name, ingredients) {
        return () => {
            sessionStorage.set("drink", {
                id: id,
                name: name,
                ingredients: ingredients
            });
            window.location.href = "/drink.html";
        }
    }

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("drink", null);

        let name = document.getElementById("create-name-field").value;

        let userId = "validUserId"

        let ingredients = document.querySelectorAll('input:checked');

        let ingredientsArray = [];

        for (let i = 0; i < ingredients.length; i++) {
            ingredientsArray = Array.from(ingredients).map(ingredient => ingredient.value);
        }

        const createdDrink = await this.client.createDrink(userId, name, ingredientsArray, this.errorHandler);
        this.dataStore.set("drink", createdDrink);

        if (createdDrink) {
            this.showMessage(`Created ${createdDrink.name}!`)

        } else {
            this.errorHandler("Error creating!  Try again...");
        }

        await this.renderDrink();
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

    await landingPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
