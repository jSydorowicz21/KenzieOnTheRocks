import BaseClass from "../util/baseClass";
import DrinkClient from "../api/drinkClient";
import UserClient from "../api/userClient";
import {createRoot} from "react-dom/client";
import React from "react";
/**
 * Logic needed for the view playlist page of the website.
 */
class DrinkPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onGet','update', 'erase', 'addToList'], this);
        this.drinkClient = new DrinkClient();
        this.userClient = new UserClient();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the restaurant/review list.
     */
    mount() {
        let drinkName = sessionStorage.getItem("drinkName");
        let ingredients = sessionStorage.getItem("ingredients");

        const root = createRoot(document.getElementById("card"));

        const drink = {
            name: drinkName,
            ingredients: ingredients.split(',')
        };

        this.render(drink, root);

        document.getElementById('update').addEventListener('click', this.update);
        document.getElementById('erase').addEventListener('click', this.erase);
        document.getElementById('add').addEventListener('click', this.addToList);
    }

     async onGet() {
            console.log("Getting a drink");
            const className = 'drink';
            const root = createRoot(document.getElementById("drink-info"));
            getAndRenderDrinks(root, drinks, className);
        }

     async update(event) {
        event.preventDefault();

        let userId = sessionStorage.getItem("userId");
        let drinkId = sessionStorage.getItem("drinkId");
        let drinkName = document.getElementById("update-name-field").value;
        let ingredients = document.querySelectorAll("select");

        //toggle the update window

        let ingredientsArray = [];

        for (let i = 0; i < ingredients.length; i++) {
            ingredientsArray = Array.from(ingredients).map(ingredient => String(" " + ingredient.value));
        }

        const updatedDrink = await this.drinkClient.updateDrink(drinkId, drinkName, ingredientsArray, userId);
        const root = createRoot(document.getElementById("card"));
        this.render(updatedDrink, root);

        if (updatedDrink) {
            this.showMessage('Drink updated successfully, redirecting to home page...');
            await new Promise(r => setTimeout(r, 3000))
            window.location.href = "index.html"
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
    }

    async erase(event) {
        event.preventDefault();
        this.showMessage("Attempting to delete drink please wait...");
        let drinkId = sessionStorage.getItem("drinkId");
        console.log("drinkId");
        let result = await this.drinkClient.deleteDrink(drinkId);

        if(result != null) {
            this.showMessage('Drink deleted successfully, redirecting to home page...');
            await new Promise(r => setTimeout(r, 3000))
            window.location.href = "index.html"
        }
        else {
            this.errorHandler("Error deleting drink");
        }
    }

    async addToList(event) {
        event.preventDefault();
        let userId = sessionStorage.getItem("userId");
        let drinkId = sessionStorage.getItem("drinkId");
        let drinkName = sessionStorage.getItem("drinkName");
        let ingredients = sessionStorage.getItem("ingredients");

        const drink = {
            userId: userId,
            id: drinkId,
            name: drinkName,
            ingredients: ingredients.split(',')
        };

        let result = await this.userClient.addToList(userId, drink);

        if(result) {
            this.showMessage('Drink added successfully, redirecting to home page...');
            await new Promise(r => setTimeout(r, 3000))
            window.location.href = "index.html"
        }
        else {
            this.errorHandler("Error adding drink to list!  Try again...");
        }

    }

    render(drink, root) {
        let html = (
        <div class = "drink">
            <h1>Drink Name: {drink.name}</h1>
            <h3>Ingredients: </h3>
            <div id="drinkvalues">{drink.ingredients}</div>
        </div>);
        root.render(html);
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