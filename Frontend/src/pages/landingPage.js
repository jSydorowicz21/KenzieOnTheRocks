import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import DrinkClient from "../api/drinkClient";
import * as bootstrap from 'bootstrap';

/**
 * Logic needed for the view playlist page of the website.
 */
class LandingPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onGetAllDrinks', 'onCreate', 'renderDrink'], this);
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

        this.dataStore.addChangeListener(this.renderDrink)
        await this.onGetAllDrinks();

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
        event.preventDefault();

            let result = await this.client.getHomeDrinks();
            this.dataStore.set("drinks", result);
            if (result) {
                        const paginatedList = document.getElementById("drink-list");
                    } else {
                        this.errorHandler("Error doing GET!  Try again...");
                    }
        const appendPageNumber = (index) => {
            const pageNumber = document.createElement("button");
            pageNumber.className = "pagination-number";
            pageNumber.innerHTML = index;
            pageNumber.setAttribute("page-index", index);
            pageNumber.setAttribute("aria-label", "Page " + index);

            paginationNumbers.appendChild(pageNumber);
        };

        const getPaginationNumbers = () => {
            for (let i = 1; i <= pageCount; i++) {
                appendPageNumber(i);
            }
        };
        window.addEventListener("load", () => {
            getPaginationNumbers();
        });
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
    landingPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
