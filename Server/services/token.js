
const jwt = require('jsonwebtoken');

const key = "Some super secret key shhhhhhhhhhhhhhhhh!!!!!";

function generateToken(data) {
  return jwt.sign(data, key);
}

function verifyToken(token) {
  try {
    return jwt.verify(token, key);
  } catch (err) {
    throw new Error('Invalid Token');
  }
}

module.exports = {
  generateToken,
  verifyToken
};
