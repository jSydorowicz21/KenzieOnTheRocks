import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import UserClient from "../api/userClient";
/**
 * Logic needed for the view playlist page of the website.
 */
class LoginPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['login', 'create'], this);
        this.dataStore = new DataStore();
        this.userClient = new UserClient();

    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the restaurant/review list.
     */
    mount() {
        //document.getElementById('get-userId-form').addEventListener('click', this.login);
        document.getElementById('create-button').addEventListener('click', this.create);
        document.getElementById('login-button').addEventListener('click', this.login);

        this.dataStore.addChangeListener(this.login)
        await this.login()

    }

    async login(event) {
        event.preventDefault();

        let userId = document.getElementById("username-field").value;

        let result = await this.userClient.getUserById(userId);

        if(result) {
            sessionStorage.setItem("userId", userId);
            console.log("User logged in");


        if (sessionStorage.getItem("userId") != null) {
            this.showMessage(`Welcome ${sessionStorage.getItem("userId")}!`)
            window.location.href = "index.html";
        } else {
            this.errorHandler("Error logging in!  Try again...");
        }
        }
    }

    async create(event) {
        event.preventDefault();

        let userId = document.getElementById("username").value;

        let result = await this.userClient.createUser(userId);

        if(result) {
            console.log("User created");
            sessionStorage.setItem("userId", userId);
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