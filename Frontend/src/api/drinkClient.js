import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently, the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class DrinkClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getHomeDrinks', 'createDrink', 'getDrinkById', 'deleteDrink', 'updateDrink', 'getFilteredDrink'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;

        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    /**
     * Gets the concert for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */
    async getHomeDrinks(errorCallback) {
        try {
            const response = await this.client.get(`/drinks/all`);
            return response.data;
        } catch (error) {
            this.handleError("getHomeDrinks", error, errorCallback)
        }
    }

    async createDrink(userId, name, ingredients, errorCallback) {
        try {
            const response = await this.client.post(`/drinks`, {
                ingredients: ingredients,
                name: name,
                userId: userId
            });
            return response.data;
        } catch (error) {
            this.handleError("createDrink", error, errorCallback);
        }
    }

    async getDrinkById(id, errorCallBack) {

        try {
            const response = await this.client.get(`/drinks/${id}`);
            return response.data;
        } catch (error){
            this.handleError("getDrinkById", error, errorCallBack);
        }

    }

    async deleteDrink(id, errorCallBack) {

        try {
            const response = await this.client.delete(`/drinks/${id}`, {
            });
            return response.data;
        } catch (error){
            this.handleError("deleteDrink", error, errorCallBack);
        }

    }

    async updateDrink(id, name, ingredients, userId, errorCallBack) {

        try {
            const response = await this.client.put(`/drinks/`, {
                "id": id,
                "name": name,
                "ingredients": ingredients,
                "userId": userId
            });
            return response.data;
        } catch (error){
            this.handleError("updateDrink", error, errorCallBack);
        }

    }

    async getFilteredDrink(ingredients, errorCallBack) {

        try {
            const response = await this.client.post(`/drinks/filter`, ingredients);
            return response.data;

        } catch (error){
            this.handleError("getFilteredDrink", error, errorCallBack);
        }

    }
    //use as onGet for search button in landingPage
    //filter is value of checked query box
    //use .contains

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
