const Price = require('../models/price');

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
        // Find all prices for the given store name and item IDs
        const itemIds = items.map(item => item._id);

        // Assuming storeName is unique per store, you may need to adjust this if it's not
        const store = await Store.findOne({ name: storeName });
        if (!store) throw new Error('Store not found');

        const prices = await Price.find({
            itemId: { $in: itemIds },
            storeId: store._id
        });

        let totalPrice = 0;
        items.forEach(item => {
            const priceInfo = prices.find(price => price.itemId.equals(item._id));
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