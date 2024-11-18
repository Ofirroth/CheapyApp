const priceService = require('../services/price');

const getItemPriceByStore = async (req, res) => {
    try {
        const { storeId,itemId } = req.body;
        const price = await priceService.getItemPriceByStore(storeId, itemId);
        console.log(price);
        res.status(200).send(price.toString());
    } catch (error) {
        console.error(error);
        res.status(500).json('Internal Server Error');
    }
};

const getTotalPriceByStore = async (req, res) => {
    try {
        const { items, storeName } = req.body;
        if (!items || !storeName) {
            return res.status(400).json({ error: "items or storeName not provided" });
        }
        const totalPrice = await priceService.getTotalPriceByStore(items, storeName);
        res.status(200).send(totalPrice.toString());
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: error.message });
    }
};



module.exports = { getItemPriceByStore, getTotalPriceByStore };