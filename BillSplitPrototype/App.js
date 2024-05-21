import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet, SafeAreaView, Modal, TouchableOpacity } from 'react-native';

const BillSplitter = () => {
  const [personCount, setPersonCount] = useState(0);
  const [people, setPeople] = useState([]);
  const [items, setItems] = useState([]);
  const [totalTax, setTotalTax] = useState(0);
  const [results, setResults] = useState([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [currentInput, setCurrentInput] = useState('');
  const [inputType, setInputType] = useState('');

  const handlePersonCountChange = text => {
    setPersonCount(Number(text));
  };

  const handleAddPerson = () => {
    setPeople(oldPeople => [...oldPeople, { name: currentInput, items: [] }]);
    setModalVisible(false);
  };

  const handleAddItem = () => {
    const splitInput = currentInput.split(',');
    setItems(oldItems => [...oldItems, { name: splitInput[0], cost: parseFloat(splitInput[1]) }]);
    setModalVisible(false);
  };

  const handleCalculate = () => {
    let subtotal = items.reduce((acc, item) => acc + item.cost, 0);
    let finalResults = people.map(person => {
      let personSubtotal = person.items.reduce((acc, itemIndex) => acc + items[itemIndex].cost, 0);
      let tip = parseFloat(currentInput); // Assume input is the tip percentage
      let personTip = (personSubtotal * tip) / 100;
      let personTax = (personSubtotal / subtotal) * totalTax;
      let total = personSubtotal + personTip + personTax;
      return { name: person.name, total: total.toFixed(2) };
    });
    setResults(finalResults);
    setModalVisible(false);
  };

  const openModalForInput = (type) => {
    setInputType(type);
    setModalVisible(true);
  };

  return (
    <SafeAreaView style={styles.mainContainer}>
      <Modal
        animationType="slide"
        transparent={true}
        visible={modalVisible}
        onRequestClose={() => {
          setModalVisible(!modalVisible);
        }}
      >
        <View style={styles.centeredView}>
          <View style={styles.modalView}>
            <TextInput
              style={styles.modalInput}
              onChangeText={setCurrentInput}
              value={currentInput}
              placeholder={`Enter ${inputType === 'item' ? 'name, cost' : 'name or percentage'}`}
            />
            <Button
              title="Submit"
              onPress={() => {
                inputType === 'person' ? handleAddPerson() :
                inputType === 'item' ? handleAddItem() : handleCalculate();
              }}
            />
          </View>
        </View>
      </Modal>

      <TouchableOpacity style={[styles.button, {top: 20}]} onPress={() => openModalForInput('person')}>
        <Text>Add Person</Text>
      </TouchableOpacity>
      <TouchableOpacity style={[styles.button, {top: 70}]} onPress={() => openModalForInput('item')}>
        <Text>Add Item</Text>
      </TouchableOpacity>
      <TouchableOpacity style={[styles.button, {top: 120}]} onPress={() => openModalForInput('calculate')}>
        <Text>Calculate</Text>
      </TouchableOpacity>
      {results.map((result, index) => (
        <Text key={index} style={styles.resultText}>{result.name} owes ${result.total}</Text>
      ))}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  mainContainer: {
    // marginTop: 40,
    justifyContent: 'center',
    position: 'absolute',
    alignSelf: 'center',
  },
  button: {
    position: 'absolute',
    left: 20,
    backgroundColor: '#ddd',
    padding: 10,
  },
  centeredView: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    marginTop: 22
  },
  modalView: {
    margin: 20,
    backgroundColor: "white",
    borderRadius: 20,
    padding: 35,
    alignItems: "center",
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5
  },
  modalInput: {
    marginBottom: 15,
    borderWidth: 1,
    borderColor: 'gray',
    width: 200,
    padding: 10,
  },
  resultText: {
    position: 'absolute',
    top: 300, // Adjust accordingly
    left: 20, // Adjust accordingly
    fontSize: 18,
    color: 'darkblue',
  }
});

export default BillSplitter;
