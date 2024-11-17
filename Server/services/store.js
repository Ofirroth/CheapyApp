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

module.exports = { getStoresByCity, getAllStores };
