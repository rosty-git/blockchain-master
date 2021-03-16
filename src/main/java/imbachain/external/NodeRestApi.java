package imbachain.external;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import imbachain.infrastructure.BaseNode;
import imbachain.infrastructure.Block;
import imbachain.infrastructure.NodeInterface;
import imbachain.infrastructure.Transaction;

@RestController
public class NodeRestApi {

	@Autowired
	private NodeInterface node;

	@GetMapping(value = "/peers")
	public Collection<BaseNode> getPeers() {
		return node.getPeers();
	}

	@PostMapping(value = "/peers")
	public ResponseEntity<BaseNode> addPeer(@RequestBody BaseNode newPeer) {
		if (node.addPeer(newPeer)) {
			return new ResponseEntity<BaseNode>(newPeer, HttpStatus.OK);
		}
		return new ResponseEntity<BaseNode>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value = "/transactions")
	public Collection<Transaction> getTransactions() {
		return node.getTransactions();
	}

	@PostMapping(value = "/transactions")
	public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
		if (node.addTransaction(transaction)) {
			return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
		}
		return new ResponseEntity<Transaction>(HttpStatus.BAD_REQUEST);

	}

	@GetMapping("/blocks")
	public Collection<Block> getBlocks() {
		return node.getBlocks();
	}
	
	@PostMapping("/blocks")
	public ResponseEntity<Block> addBlock(@RequestBody Block newBlock) {
		if (node.addBlock(newBlock)) {
			return new ResponseEntity<Block>(newBlock, HttpStatus.OK);
		}
		return new ResponseEntity<Block>(HttpStatus.CONFLICT);
	}
	
	@GetMapping("/blocks/{id}")
	public ResponseEntity<Block> getBlocks(@PathVariable("id") int id) {
		try {
			Block block = node.getBlock(id);
			return getOK(block);
		} catch (Exception e) {
			return new ResponseEntity<Block>(HttpStatus.NOT_FOUND);
		}
	}

	private <T> ResponseEntity<T> getOK(T result) {
		return new ResponseEntity<T>(result, HttpStatus.OK);
	}

}