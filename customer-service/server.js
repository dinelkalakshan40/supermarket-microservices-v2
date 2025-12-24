const express =require('express');
const connectDB=require('./db');
const Customer =require('./model/customer')
const  app =express();

const eurekaClient =require('./config/eureka')

app.use(express.json());

connectDB();



app.post('/customers/addCustomer',async (req,res)=>{
    try{
        const customer =new Customer(req.body);
        await customer.save();
        res.status(201).json(customer);
    }catch (err){
        res.status(400).json({error:err.message})
    }
})

app.get('/customers/getAllCustomers', async (req,res)=>{
    try {
        const customers=await Customer.find();
        res.json(customers);
    }catch (err){
        res.status(500).json({error:err.message})
    }
})

app.get('/customers/getCustomer/:id', async (req,res)=>{
    try {
        const customer=await Customer.findOne({customerId:req.params.id});
        if(!customer) return res.status(404).json({error:'Customer not found'});
        res.json(customer);
    }catch (err){
        res.status(500).json({error:err.message});
    }
});

app.put('/customers/updateCustomer/:id', async (req,res) =>{
    try {
        const customer =await Customer.findOneAndUpdate(
            {customerId:req.params.id},
            req.body, {new:true,runValidators:true}
        );
        if(!customer) return res.status(404).json({error:"Customer not found"});
        res.json(customer);
    }catch (err){
        res.status(500).json({error:err.message});
    }
});

app.delete('/customers/deleteCustomer/:id', async (req, res) => {
    try {
        const customer = await Customer.findOneAndDelete({ customerId: req.params.id });
        if (!customer) return res.status(404).json({ error: 'Customer not found' });
        res.json({ message: 'Customer deleted' });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

const PORT = 3000
app.listen(PORT,()=>{
    console.log(`Server is running on http://localhost:${PORT}`);
    eurekaClient.start(err => {
        if (err) console.error('Eureka registration failed', err);
        else console.log('Customer Service registered with Eureka');
    });
});