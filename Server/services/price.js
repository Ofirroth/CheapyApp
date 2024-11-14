const mongoose = require('mongoose');
const Price = require('../models/price');
const Store = require('../models/store');

const getItemPriceByStore = async (storeId, itemId) => {
    try {
         const priceRecord = await Price.findOne({
         'itemId': itemId,
          'storeId': storeId
          });
         if (!priceRecord) {
            throw new Error('Price not found');
         }
        return priceRecord.price;
    } catch (error) {
        throw new Error('Error fetching items by category');
    }
};

const getTotalPriceByStore = async (items, storeName) => {
    try {
        if (!items || items.length === 0) {
            throw new Error('No items provided');
        }

        const itemIds = items.map(item => item._id.toString());

        const store = await Store.findOne({ name: storeName });
        const storeId = store._id.toString();
        if (!store) throw new Error('Store not found');

        const prices = await Price.find({
            'itemId': { $in: itemIds },
            'storeId': storeId
        });
        let totalPrice = 0;
        items.forEach(item => {
            const priceInfo = prices.find(price => price.itemId === item._id.toString());
            if (priceInfo) {
                totalPrice += priceInfo.price * item.quantity;
            }
        });
        return totalPrice;
    } catch (error) {
        throw new Error(`Error calculating total price for store ${storeName}: ${error.message}`);
    }
};


module.exports = { getItemPriceByStore, getTotalPriceByStore };