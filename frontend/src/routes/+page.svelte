<script>

    import { onMount } from 'svelte';
    import { cars, searchTerm, pageNo, pageSize } from '../stores/cars';
    import { searchCars, deleteCar } from '../services/carService';

    async function loadCars() {
        $searchTerm = '';
        $cars = [];
        let carList = [];
        cars.subscribe(value => {
            carList = value;
        })();
        if (!carList || carList.length === 0) {
            const response = await searchCars('', $pageNo, 100); 
            cars.set(response.data.content);
        }
    }

    onMount(loadCars);

    async function handleDelete(carId) {
        const success = await deleteCar(carId);
        if (success) {
            cars.update(cars => cars.filter(car => car.id !== carId));
        }
    }

    async function handleSearch() {
        const carList = await searchCars($searchTerm, $pageNo, $pageSize);
        if (carList.success) {
            cars.set(carList.data.content);
        }
    }

    function handleKeyDown(event) {
        if (event.key === 'Enter') {
            handleSearch();
        }
    }

</script>

<h1>Car Management</h1>

<input 
    type="text" 
    placeholder="Search by model" 
    bind:value={$searchTerm} 
    on:keydown={handleKeyDown} 
/>

<button on:click={handleSearch}>Search</button>

<ul>
    {#each $cars as car}
    <li>
        <span class="car-info">{car.brand} {car.model} - {car.kmsDriven} kms - {car.transmission}</span>
        <button on:click={() => handleDelete(car.id)}>Delete</button>
    </li>
    {/each}
</ul>

<style>

    h1 {
        text-align: center;
        margin-bottom: 1.5rem;
    }

    input[type="text"] {
        padding: 0.5rem;
        border: 2px solid #ccc;
        border-radius: 4px;
        width: 300px;
        display: block;
        margin: 0 auto;
        transition: border-color 0.3s ease-in-out;
    }

    input[type="text"]:focus {
        outline: none;
        border-color: #007BFF;
    }

    button {
        padding: 0.5rem 1rem;
        border: none;
        background-color: #007BFF;
        color: white;
        border-radius: 4px;
        cursor: pointer;
        display: block;
        margin: 1rem auto;
    }

    button:hover {
        background-color: #0056b3;
    }

    ul {
        list-style-type: none;
        padding: 0;
    }

    ul li {
        background-color: #f9f9f9;
        margin: 0.5rem auto;
        padding: 0.75rem;
        width: 300px;
        border-radius: 4px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .car-info {
        flex-grow: 1;
    }

    ul li button {
        background-color: #FF4D4D;
        margin-left: 1rem;
    }

    ul li button:hover {
        background-color: #CC0000;
    }
    
</style>