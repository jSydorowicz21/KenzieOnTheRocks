import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import DrinkClient from "../api/drinkClient";
import UserClient from "../api/userClient";
/**
 * Logic needed for the view playlist page of the website.
 */
class LoginPage extends BaseClass {

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
        document.getElementById('get-userId-form').addEventListener('submit', this.login);
        document.getElementById('create-button').addEventListener('click', this.create);

    }

    async update(event) {
        event.preventDefault();

        let userId = sessionStorage.getItem('userId');

        let result = await this.drinkClient.updateDrink();

        if(result.status === 200) {
            this.showMessage('Drink updated successfully');
        }
        else {
            this.errorHandler("Error updating drink");
        }

    }

    async delete(event) {
        event.preventDefault();

        let userId = document.getElementById("username").value;

        let result = this.userClient.createUser(userId);

        if(result) {
            console.log("User created");
            sessionStorage.setItem("userId", userId.value);
            window.location.href = "index.html";
        }
        else {
            this.errorHandler("Error creating user!  Try again...");
        }

    }

    async addToList(event) {
        event.preventDefault();

        let userId = document.getElementById("username").value;

        let result = this.userClient.createUser(userId);

        if(result) {
            console.log("User created");
            sessionStorage.setItem("userId", userId.value);
            window.location.href = "index.html";
        }
        else {
            this.errorHandler("Error creating user!  Try again...");
        }

    }

}
/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const loginPage = new LoginPage();

    if (sessionStorage.getItem("userId") != null){
        window.location.href = "index.html";
    }

    loginPage.mount();

};

window.addEventListener('DOMContentLoaded', main);