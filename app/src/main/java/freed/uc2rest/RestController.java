package freed.uc2rest;

public class RestController
{
    private RestClient restClient;

    public RestController()
    {}

    public void setUrl(String url)
    {
        restClient = new RestClient(url);
    }

    public RestClient getRestClient()
    {
        return restClient;
    }
}
