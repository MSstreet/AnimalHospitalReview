import axios from 'axios';

const instance = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL
});

instance.interceptors.request.use(function (config) {
    const token = localStorage.getItem('user_token');
    config.headers.Authorization = "Bearer " + token;
    return config;
});

instance.interceptors.response.use(function (config) {
    return config
});

export default instance;