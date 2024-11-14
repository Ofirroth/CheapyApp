const mongoose = require('mongoose');
const Price = require('../models/price');
const Store = require('../models/store');

const getItemPriceByStore = async (itemId, storeName) => {
    try {
         const store = await Store.findOne({ name: storeName });
         if (!store) {
            throw new Error('Store not found');
         }
         const priceRecord = await Price.findOne({ itemId: itemId, storeId: store._id });

        if (!priceRecord) {
            throw new Error('Price record not found');
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

        const itemFirst = items[0];
        itemId = itemFirst._id.toString();
        console.log(itemId);

        const store = await Store.findOne({ name: storeName });
        const storeId = store._id.toString();
        console.log(storeId);
        if (!store) throw new Error('Store not found');

        const prices = await Price.find({
            'itemId': itemId,
            'storeId': storeId
        });
        console.log(prices[0]);

        let totalPrice = 0;
        items.forEach(item => {
            const priceInfo = prices.find(price => price.itemId.equals(item._id.toString()));
            if (priceInfo) {
                totalPrice += priceInfo.price * item.quantity;
            }
        });
        console.log(totalPrice);

        return totalPrice;
    } catch (error) {
        throw new Error(`Error calculating total price for store ${storeName}: ${error.message}`);
    }
};


module.exports = { getItemPriceByStore, getTotalPriceByStore };