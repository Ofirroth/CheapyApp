const { findBarcode } = require('../services/barcode');

exports.searchBarcode = async (req, res) => {
  const { barcode } = req.params;

  try {
    if (!barcode) {
      return res.status(400).json({ success: false, message: 'Barcode is required' });
    }

    const data = await findBarcode(barcode);
    res.status(200).json({ success: true, data });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
};
