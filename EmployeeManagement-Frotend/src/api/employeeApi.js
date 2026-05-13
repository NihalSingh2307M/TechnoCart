const BASE_URL = import.meta.env.VITE_API_BASE_URL;

if (!BASE_URL) {
  throw new Error("VITE_API_BASE_URL is not defined. Check your .env file.");
}

const request = async (url, options = {}) => {
  const res = await fetch(url, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (!res.ok) throw new Error(`HTTP ${res.status}: ${res.statusText}`);
  if (res.status === 204) return null;
  return res.json();
};

export const employeeApi = {
  getAll: (sortBy) =>
    request(sortBy ? `${BASE_URL}?sortBy=${sortBy}` : BASE_URL),

  getById: (id) =>
    request(`${BASE_URL}/${id}`),

  search: ({ name, department }) => {
    const params = new URLSearchParams();
    if (name) params.set("name", name);
    if (department) params.set("department", department);
    return request(`${BASE_URL}/search?${params}`);
  },

  create: (data) =>
    request(BASE_URL, { method: "POST", body: JSON.stringify(data) }),

  update: (id, data) =>
    request(`${BASE_URL}/${id}`, { method: "PUT", body: JSON.stringify(data) }),

  patch: (id, data) =>
    request(`${BASE_URL}/${id}`, { method: "PATCH", body: JSON.stringify(data) }),

  delete: (id) =>
    request(`${BASE_URL}/${id}`, { method: "DELETE" }),
};
