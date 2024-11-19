const recoService = require('../services/reco');

const getRecommended = async (req, res) => {
    try {
        const { username } = req.params;
        const recoItems = await recoService.getRecommended(username);
        res.status(200).json(recoItems);
    } catch (error) {
        console.error(error);
        res.status(500).json('Internal Server Error');
    }
};

module.exports = {getRecommended};