import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

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
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderDrink() {
        let resultArea = document.getElementsByClassName('drink')

        const drink = this.dataStore.get("drink");


        if (drink) {
            resultArea.innerHTML = `
                <div>Drink Name: ${drink.name}</div>
                <div>Ingredients: ${drink.ingredients}</div>
            `
        } else {
            resultArea.innerHTML = "No Item";
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
                        this.showMessage("Returned all drinks!")
                    } else {
                        this.errorHandler("Error doing GET!  Try again...");
                    }
    }

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("drink", null);

        let name = document.getElementById("create-name-field").value;

        let userId = "validUserId"

        let alcohol = document.querySelectorAll("[name=Alcohol]:checked");

        let mixer = document.querySelectorAll("[name=Mixer]:checked");

        let finisher = document.querySelectorAll("[name=Finisher]:checked");

        let ingredients = [alcohol, mixer, finisher];

        const createdDrink = await this.client.createDrink(userId, name, ingredients, this.errorHandler);
        this.dataStore.set("drink", createdDrink);

        if (createdDrink) {
            this.showMessage(`Created ${createdDrink.name}!`)

        } else {
            this.errorHandler("Error creating!  Try again...");
        }
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
