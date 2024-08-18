<nav>
    <a href="/">Home</a>
    <a href="/add_car">Add Car</a>
    <a href="/search_car">Search Cars</a>
</nav>

<slot />

<style>

    nav {
        display: flex;
        justify-content: center;
        background-color: #333;
        padding: 1rem;
    }

    nav a {
        color: white;
        text-decoration: none;
        padding: 0.5rem 1rem;
        margin: 0 0.5rem;
        border-radius: 4px;
    }

    nav a:hover {
        background-color: #575757;
    }
    
</style>