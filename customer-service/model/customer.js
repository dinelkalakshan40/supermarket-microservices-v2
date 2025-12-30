const mongoose =require('mongoose');
const customerSchema= new mongoose.Schema({
    customerId: { type: String, required: true, unique: true },
    customerName: { type: String, required: true },
    address: { type: String },
    email: { type: String, required: true },
    phone: { type: Number }
});
module.exports=mongoose.model('Customer',customerSchema);
