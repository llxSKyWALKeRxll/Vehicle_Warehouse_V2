import { writable } from 'svelte/store';

export const cars = writable([]);
export const searchTerm = writable('');
export const pageNo = writable(0);
export const pageSize = writable(100);
