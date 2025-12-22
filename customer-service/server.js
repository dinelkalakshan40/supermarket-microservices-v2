const express =require('express');
const connectDB=require('./db');
const Customer =require('./model/customer')
const  app =express();

app.use(express.json());

connectDB();

app.post('/customer',async (req,res)=>{
    try{
        const customer =new Customer(req.body);
        await customer.save();
        res.status(201).json(customer);
    }catch (err){
        res.status(400).json({error:err.message})
    }
})

app.get('/customers', async (req,res)=>{
    try {
        const customers=await Customer.find();
        res.json(customers);
    }catch (err){
        res.status(500).json({error:err.message})
    }
})

app.get('/customers/:id', async (req,res)=>{
    try {
        const customer=await Customer.findOne({customerId:req.params.id});
        if(!customer) return res.status(404).json({error:'Customer not found'});
        res.json(customer);
    }catch (err){
        res.status(500).json({error:err.message});
    }
});

app.put('/customers/:id', async (req,res) =>{
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

app.delete('/customers/:id', async (req, res) => {
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
});