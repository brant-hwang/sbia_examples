package readinglist

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

@Controller
@RequestMapping("/")
class ReadingListController {

    @Autowired
    AmazonProperties amazonProperties

    @ExceptionHandler(value=RuntimeException.class)
    @ResponseStatus(value=HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    def error() {
        "error"
    }

    @RequestMapping(method=RequestMethod.GET)
    def readersBooks(Reader reader, Model model) {
        List<Book> readingList = Book.findAllByReader(reader)
        model.addAttribute("reader", reader)
        if (readingList) {
            model.addAttribute("books", readingList)
            model.addAttribute("amazonID", amazonProperties.getAssociateId())
        }
        "readingList"
    }

    @RequestMapping(method=RequestMethod.POST)
    def addToReadingList(Reader reader, Book book) {
        Book.withTransaction {
            book.setReader(reader)
            book.save()
        }
        "redirect:/"
    }

}

