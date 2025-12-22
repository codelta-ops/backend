export async function onRequest(context) {
  const { request, params } = context;

  const backend = "https://backend-8-ach8.onrender.com";

  const url = new URL(request.url);
  const targetUrl = backend + "/api/" + params.path;

  const newRequest = new Request(targetUrl, request);

  return fetch(newRequest);
}
