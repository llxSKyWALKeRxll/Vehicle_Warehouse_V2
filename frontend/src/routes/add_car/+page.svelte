<script>

    import { addCar } from '../../services/carService';
    import { cars } from '../../stores/cars';
    import { goto } from '$app/navigation';

    let model = '';
    let brand = '';
    let kmsDriven = '';
    let transmission = '';
    let features = '';

    async function handleAddCar() {
        const car = {
            model,
            brand,
            kmsDriven,
            transmission,
            features: features !== '' ? features.split(',').map(f => f.trim()) : []
        };
        const newCar = await addCar(car);
        cars.update(currentCars => [...currentCars, newCar.data]);
        goto('/');
    }

</script>

<h1>Add New Car</h1>

<form on:submit|preventDefault={handleAddCar}>

    <input type="text" placeholder="Model" bind:value={model} required />
    <input type="text" placeholder="Brand" bind:value={brand} required />
    <input type="text" placeholder="KMs Driven" bind:value={kmsDriven} required />
    <input type="text" placeholder="Transmission" bind:value={transmission} required />
    <input type="text" placeholder="Features (comma separated)" bind:value={features} />

    <button type="submit">Add Car</button>

</form>

<style>
    
    h1 {
        text-align: center;
        margin-bottom: 20px;
    }

    form {
        display: flex;
        flex-direction: column;
        max-width: 400px;
        margin: 0 auto;
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 8px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        background-color: #f9f9f9;
    }

    input, button {
        margin-bottom: 15px;
        padding: 10px;
        font-size: 16px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

    input:focus {
        border-color: #007BFF;
        outline: none;
        box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
    }

    button {
        background-color: #007BFF;
        color: #fff;
        border: none;
        cursor: pointer;
    }

    button:hover {
        background-color: #0056b3;
    }
    
</style>
