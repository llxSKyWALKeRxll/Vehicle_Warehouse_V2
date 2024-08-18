<script>
    import { cars, searchTerm, pageNo, pageSize } from '../../stores/cars';
    import { searchCars } from '../../services/carService';
    import { onMount } from 'svelte';

    async function handleSearch() {

        const carList = await searchCars($searchTerm, $pageNo, $pageSize);
        cars.set(carList.data.content);

    }

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

    function handleKeyDown(event) {

        if (event.key === 'Enter') {
            handleSearch();
        }

    }

</script>

<h1>Search Cars</h1>

<input type="text" placeholder="Search by model" bind:value={$searchTerm} on:keydown={handleKeyDown} />
<button on:click={handleSearch}>Search</button>

<ul>
    {#each $cars as car}
    <li>{car.brand} {car.model} - {car.kmsDriven} kms - {car.transmission}</li>
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
        justify-content: space-between;
        align-items: center;
    }

</style>
