const Store = require('../models/store');
const Item = require('../models/item');

const getStoresByCity = async (city) => {
    try {
        const stores = await Store.find({ city:city });
        return stores;
    } catch (error) {
        throw new Error('Error fetching stores');
    }
};

const getAllStores = async () => {
    try {
        const stores = await Store.find()
        return stores;
    } catch (error) {
        throw new Error('Error fetching stores');
    }
};


const getStore = async (id) => {
    try {
        const store = await Store.findOne({'_id':id})
        return store;
    } catch (error) {
        throw new Error('Error fetching stores');
    }
};

const getTotalPriceByStoreName = async (storeName, items) => {
    try {
        const store = await Store.findOne({ name: storeName });
        if (!store) {
            console.error(`Store not found: ${storeName}`);
            throw new Error(`Store with name ${storeName} not found`);
        }

        const storeItems = items.filter(item => item.storeId === store._id.toString());

        if (storeItems.length === 0) {
            console.warn(`No items found for store: ${storeName}`);
        }
        const totalPrice = storeItems.reduce((total, item) => total + item.price * item.quantity, 0);
        return totalPrice;

    } catch (error) {
        console.error(`Error calculating total price for store ${storeName}: ${error.message}`);
        throw new Error(`Error calculating total price for store ${storeName}: ${error.message}`);
    }
};


module.exports = { getStoresByCity, getAllStores, getTotalPriceByStoreName, getStore };

