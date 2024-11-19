package guru.springframework.springairagexpert.bootstrap;

import guru.springframework.springairagexpert.config.VectorStoreProperties;
import org.slf4j.Logger;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import org.slf4j.LoggerFactory;

import java.util.List;

@Component
public class LoadVectorStore implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoadVectorStore.class);

  @Autowired
  private VectorStore vectorStore;

  @Autowired
  private VectorStoreProperties vectorStoreProperties;


  @Override
  public void run( String... args )
          throws Exception {

    getLog().info( "Loading vector store..." );

    if (vectorStore.similaritySearch( "Sportsman" ).isEmpty()) {
      getLog().info( "No sportsman found, loading documents into vector store..." );

      loadDocumentsIntoVectorStore();
    }

    getLog().info( "Vector store loaded" );
  }

  /**
   * Refactor parsing and loading the documents into a separate method
   */
  private void loadDocumentsIntoVectorStore() {
    vectorStoreProperties.getDocumentsToLoad()
            .forEach( document -> {
              getLog().info( "Loading document {}", document.getFilename() );

              final List<Document> splitDocuments = readAndSplitDocuments( document );

              vectorStore.add( splitDocuments );
            } );
  }

  /**
   * Use the {@linkplain TikaDocumentReader} to read and split the document
   *
   * @param document the input document, a {@linkplain Resource}
   * @return the list of documents created from the input document
   */
  private List<Document> readAndSplitDocuments( Resource document ) {
    TikaDocumentReader documentReader = new TikaDocumentReader( document );
    List<Document> documents = documentReader.get();
    TextSplitter textSplitter = new TokenTextSplitter();

    return textSplitter.apply( documents );
  }

  private Logger getLog() {
    return LOGGER;
  }
}
