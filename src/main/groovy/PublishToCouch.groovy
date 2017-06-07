import groovy.util.logging.Log4j
import groovyx.net.http.RESTClient

/**
 * Publish results to couchdb
 */
@Log4j
class PublishToCouch {
    static auto = new RESTClient(Config.autoUrl, 'application/json')
    static couch = new RESTClient(Config.couchUrl, 'application/json')

    static void main(String[] args) {
        def cli = new CliBuilder(usage: 'publishToCouch [-a assembly] [-g guid]')
        cli.with {
            a longOpt: 'assembly', args: 1, 'The name of the test suite to publish'
            g longOpt: 'guid', args: 1, 'The guid of the test suite to publish'
        }

        def options = cli.parse(args)
        if (!options) {
            return
        }

        log.info publish(options.a, options.g)
    }

    static void publish(String assembly, String guid) {
        def results = auto.get(path: "results/$assembly/$guid").data
        couch.put(path: "automation/$guid", body: results)
    }
}
