const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors'); // Import the cors middleware

const userRoutes = require('./api/userRoutes');


require('dotenv').config();

const app = express();
app.use(express.json());

const corsOptions = {
  origin: ['http://127.0.0.1:5173', 'http://localhost:3000'], // Allow requests from both frontend origins
  credentials: true, // To allow cookies
  optionsSuccessStatus: 200,
};

app.use(cors(corsOptions));

// Middleware to set CORS headers
// app.use((req, res, next) => {
//   res.header("Access-Control-Allow-Origin", "http://localhost:5173"); // Update to match the domain of your app
//   res.header(
//     "Access-Control-Allow-Headers",
//     "Origin, X-Requested-With, Content-Type, Accept, Authorization"
//   );
//   res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//   next();
// });

mongoose
  .connect(process.env.MONGO_URI)
  .then(() => {
    //listen for requests
    app.listen(process.env.PORT, () => {
      console.log('connected to db and listening on port', process.env.PORT);
    });
  })
  .catch((error) => {
    console.log(error);
  });

// Use the user router
app.use('/api/users', userRoutes);


module.exports = app;
