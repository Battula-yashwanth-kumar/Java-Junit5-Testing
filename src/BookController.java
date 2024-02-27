import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import javax.naming.NameNotFoundException;

import org.springframework.web.bind.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value='/book')
public class BookController {
    @Autowired
    BookRespository bookRepository;

    @GetMApping
    public List<Book> getAllBookRecords(){
        return bookRepository.findAll();
    }
    @GetMApping(value='{bookId}')
    public Book getBookById(@PathVariable(value='bookId') Long bookId){
        return BookRespository.findById(bookId).get();
    }
    @PostMapping
    public Book createBookRecord(@RequestBody @valid Book bookRecord){
        return bookRepository.save(bookRecord);
    }

    @PutMapping
    public Book updateBookRecord(@RequestBody @valid Book bookRecord){
        if(bookRecord==null || bookRecord.getBookId()==null){
            throw new NameNotFoundException('BookRecord or ID must not be null');
        }
        Optional <Book> optionalBook=bookRespository.findByID(bookRecord.getBookID());
        if(!optionalBook.isPresent()){
            throw new NameNotFoundException(
                "BooK with ID:"+bookRecord.getBookId()+"does not exist");
            )
        }


        Book existingBookRecord=optionalBook.get();
        existingBookRecord.setName(bookRecord.getName());
        existingBookRecord.setSummary(bookRecord.getSummary());
        existingBookRecord.setRating(bookRecord.getReating);

        return bookRepository.save(existingBookRecord);
    }

    @DeleteMapping(value='{bookId}')
    public void deleteBookByID(@PathVariable(value='bookId')Long bookId) throws NotFoundException{
        if(!bookRepository.findById(bookId).isPresent()){
            throw new NotFoundException("bookId" + bookId+"not present");
        }
        bookRepository.deleteById(bookId);
    }
    
}
