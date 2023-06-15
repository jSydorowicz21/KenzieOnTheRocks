import BaseClass from "../util/baseClass";
import DrinkClient from "../api/drinkClient";
import UserClient from "../api/userClient";
import getAndRenderDrinks from "../util/helperMethods";
import { createRoot } from "react-dom/client";

'use strict';

/**
 * Logic needed for the view playlist page of the website.
 */
class LandingPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onGetAllDrinks', 'onCreate',  'onGetUserDrinks', 'onGetFiltered'], this);
    }

    /**
     * Once the page has loaded, set up the event handlers and fet and render drink lists.
     */
    async mount() {
        document.getElementById('createButton').addEventListener('click', this.onCreate);
        document.getElementById('homeButton').addEventListener('click', this.onGetAllDrinks);
        document.getElementById('filterButton').addEventListener('click', this.onGetFiltered);

        this.client = new DrinkClient();
        this.userClient = new UserClient();
        this.onGetAllDrinks();
        this.onGetUserDrinks();
    }

    // Event Handlers --------------------------------------------------------------------------------------------------
    async onGet(event) {
        event.preventDefault();
        let ingredients = document.getElementById("ingredient-string").value;
        let result = await this.client.getFilteredDrink(ingredients, this.errorHandler);

        if (result) {
            this.showMessage(`Got ${result.name}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
        document.querySelector('input[name="filter"]:checked').checked = false;
    }

    async onGetAllDrinks() {
        console.log("Getting all drinks");
        const className = 'drink';
        const drinks = await this.client.getHomeDrinks();
        const root = createRoot(document.getElementById("drink-list"));
        getAndRenderDrinks(root, drinks, className);
    }

    async onGetUserDrinks() {
        console.log("Started getting drinks for user: " + sessionStorage.getItem("userId"));
        const className = 'side-drink';

        let drinks = await this.userClient.getUsersDrinks(sessionStorage.getItem("userId"));
        const sidebar = createRoot(document.getElementById("sidebar"));

        if (drinks == null){
            document.getElementById("sidebar").innerHTML = "";
        }
        else {
            getAndRenderDrinks(sidebar, drinks, className);
        }
    }

    async onCreate(event) {
        event.preventDefault();
        console.log("Starting drink creation");

        let name = document.getElementById("create-name-field").value;
        let userId = sessionStorage.getItem("userId");
        let ingredients = document.getElementById("create-form").querySelectorAll('select');//input:checked
        let ingredientsArray = [];

        for (let i = 0; i < ingredients.length; i++) {
            ingredientsArray = Array.from(ingredients).map(ingredient => String(" " + ingredient.value));
        }

        this.showMessage(`Creating ${name}!`);
        const createdDrink = await this.client.createDrink(userId, name, ingredientsArray, this.errorHandler);

        if (createdDrink) {
            this.showMessage(`Created ${createdDrink.name}!`)
            await this.onGetAllDrinks();
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
    }

    async onGetFiltered(event) {
        event.preventDefault();
        console.log("Started filtering drinks")
        let ingredients = document.getElementById("filter-form").querySelectorAll('select'); //input:checked
        const className = 'drink';
        let ingredientsArray = [];

        for (let i = 0; i < ingredients.length; i++) {
            ingredientsArray = Array.from(ingredients).map(ingredient => String(" " + ingredient.value));
        }

        this.showMessage(`Searching for matches...`);
        const drinks = await this.client.getFilteredDrink(ingredientsArray, this.errorHandler);

        if (drinks) {
            this.showMessage(`Matches found, see drinks box!`);
            const root = createRoot(document.getElementById("drink-list"));
            getAndRenderDrinks(root, Array.from(drinks), className);
        } else {
            this.errorHandler("Unable to find matches, please refine search.");
        }
    }
}

/**
 * Main method to run when the page contents have loaded. Has variable checking to force login and clear drinks.
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