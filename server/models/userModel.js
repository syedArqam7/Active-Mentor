const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const dotenv = require('dotenv');

dotenv.config({path: './config.env'});

const userSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true,
  },
  email: {
    type: String,
    required: true,
    unique: true,
  },
  phone: {
    type: Number,
    required: true,
    unique: true,
  },
  password: {
    type: String,
    required: true,
  },
  tokens: [
    {
      token: {
        type: String,
        required: true,
      },
    },
  ],
});

// Hash the password before saving the user model
userSchema.pre('save', async function (next) {
  if (this.isModified('password')) {
    this.password = await bcrypt.hash(this.password, 12);
  }
  next();
});

// Generate authentication token
userSchema.methods.generateAuthToken = async function () {
  try {
    const token = jwt.sign({_id: this._id}, process.env.JWT_SECRET);
    this.tokens = this.tokens.concat({token});
    await this.save();
    return token;
  } catch (error) {
    console.log(error);
  }
};

// Create User model
const User = mongoose.model('User', userSchema);

module.exports = User;
