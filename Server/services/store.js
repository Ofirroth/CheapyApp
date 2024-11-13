const Store = require('../models/store');
const Item = require('../models/item');

const getStoresByAdress = async (adress) => {
    try {
        const stores = await Store.find({ adress }).populate('store');  // Populates store data
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

const getTotalPriceByStoreName = async (storeName) => {
    try {
        const store = await Store.findOne({ name: storeName });
        if (!store) {
            throw new Error('Store not found');
        }
        const items = await Item.find({ store: store._id });
        const totalPrice = items.reduce((total, item) => total + item.price * item.quantity, 0);
        return totalPrice;
    } catch (error) {
        throw new Error(`Error calculating total price for store ${storeName}`);
    }
};

module.exports = { getStoresByAdress, getAllStores, getTotalPriceByStoreName };
