const BASE_URL = 'http://localhost:8080/v1/car';

export async function addCar(car) {

    const response = await fetch(`${BASE_URL}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(car),
    });
    return response.json();

}

export async function deleteCar(carId) {

    const response = await fetch(`${BASE_URL}?carId=${carId}`, {
        method: 'DELETE',
    });
    return response.ok;

}

export async function searchCars(searchTerm, pageNo, pageSize) {

    const response = await fetch(`${BASE_URL}/search?searchTerm=${searchTerm}&pageNo=${pageNo}&pageSize=${pageSize}`);
    return response.json();
    
}
