const storeService = require('../services/store');

const getStores = async (req, res) => {
    try {
        const stores = await storeService.getAllStores();
        console.log(stores);
        res.status(200).json(stores);
    } catch (error) {
        console.error(error);
        res.status(500).json('Internal Server Error');
    }
};

const getTotalPriceByStore = async (req, res) => {
    const { storeName } = req.params;
    try {
        const totalPrice = await storeService.getTotalPriceByStoreName(storeName);
        res.status(200).json({ storeName, totalPrice });
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: error.message });
    }
};

module.exports = { getStores, getTotalPriceByStore };